import os
import sys
#can decode TBLU

def isBIN1File(file):
	filedata = file.read(20)
	if filedata[:4] == b'BIN1':
		return True
		print("valid BIN1 file has been loaded")
	else:
		return False
		print("please enter a valid BIN1 file")

def readHexFromBIN1File(file, offset, length):
	file.seek(offset)
	hexdata = file.read(length)
	hexstr = ''
	for h in hexdata:
		hexstr = hex(h)[2:].zfill(2) + hexstr
	return hexstr

def readStrFromBIN1File(file, offset, length):
	file.seek(offset)
	return file.read(length).decode()

def readStrFromOffset(file, strOffsetOffset):
	atOffset = strOffsetOffset
	strOffset = readHexFromBIN1File(file, atOffset, 0x4)
	strOffset = int(strOffset, 16) + 0x10
	strLength = readHexFromBIN1File(file, strOffset - 0x4, 0x4)
	strLength = int(strLength, 16)
	string = readStrFromBIN1File(file, strOffset, strLength)
	string = string[:-1]
	return string

def readPartFromBIN1File(file, fromOffset, toOffset):
	if isinstance(fromOffset, str):
		fromOffset = int(fromOffset, 16)
	if isinstance(toOffset, str):
		toOffset = int(toOffset, 16)
	file.seek(fromOffset)

	length = toOffset - fromOffset

	hexdata = file.read(length)
	hexstr = ''
	i = 0
	for h in hexdata:
		hexstr += hex(h)[2:].zfill(2)
		i += 1
	return hexstr

def writeToCSV(data):
	if sys.argv[2] == "CSV" or sys.argv[2] == "BOTH":
		decodedBIN1File = sys.argv[1] + '.BIN1decoded.csv'

		with open(decodedBIN1File, 'a') as jf:
			jf.write(data)

def writeToJson(data):
	if sys.argv[2] == "JSON" or sys.argv[2] == "BOTH":
		decodedBIN1File = sys.argv[1] + '.BIN1decoded.JSON'

		with open(decodedBIN1File, 'a') as jf:
			jf.write(data)

def connectJsonParts():
	if sys.argv[2] == "JSON" or sys.argv[2] == "BOTH":
		decodedBIN1File = sys.argv[1] + '.BIN1decoded.JSON'

		with open(decodedBIN1File, 'r+') as f:
			text = f.read()
			fixedText = text.replace('}\n{', '},\n{')
			f.seek(0)
			f.write(fixedText)
			f.truncate()

def readPointer(file, offset):
	pointerIndicator = readHexFromBIN1File(file, offset + 0x3, 0x1)
	if(str(int(pointerIndicator)) == "40"):
		length = readHexFromBIN1File(file, offset, 0x3)
		offset += 0x8
		location = readHexFromBIN1File(file, offset, 0x4)
		return location, length
	else:
		return "null"

def scanForAdress(file, fromOffset):
	if readHexFromBIN1File(file, fromOffset, 0x4) != "ffffffff" and readHexFromBIN1File(file, fromOffset + 0x8, 0x4) == readHexFromBIN1File(file, fromOffset + 0x10, 0x4):
		startOffset = readHexFromBIN1File(file, fromOffset, 0x4)
		startOffset = int(startOffset, 16) + 0xC
		fromOffset += 0x8
		endOffset = readHexFromBIN1File(file, fromOffset, 0x4)
		endOffset = int(endOffset, 16) + 0xC
	else:
		return "null"
	try:
		return startOffset, endOffset
	except:
		return "null"

def readData(file, type, fromOffset, toOffset):
	atOffset = fromOffset
	numFiles = readHexFromBIN1File(file, atOffset, 0x4)
	numFiles = int(numFiles, 16)
	atOffset += 0x4
	print("  Type " + str(type) + " found")
	#print("  from: " + str(hex(fromOffset)) + ", to: " + str(hex(toOffset)))
	print("  amount: " + str(numFiles))

	if type == 0:
		blockSize = 0x60
		d = {'key':'value'}
		originalOffset = atOffset

		for f in range(numFiles):

			string = readStrFromOffset(file, atOffset + 0x40)
			d[str(f)] = str(string)

			atOffset += (0xA8)

		#print(d)
		atOffset = originalOffset
		for f in range(numFiles):
			item = readPartFromBIN1File(file, atOffset, atOffset + 0x48)
			writeToJson("{\n")
			ID = readHexFromBIN1File(file, atOffset + 0xc, 0x4)
			ID = int(ID,16)
			if str(ID) in d:
				parent = d.get(str(ID))
			else:
				ID = hex(ID)
				parent = ID
			writeToJson('    "parent": "' + str(parent) + '",\n')
			entityType = str(item[64:72])
			writeToJson('    "entityType": "' + entityType + '",\n')
			writeToJson('    "hash": "' + str(item[80:96]) + '",\n')
			string = readStrFromOffset(file, atOffset + 0x40)
			writeToJson('    "string": "' + string + '"')
			writeToCSV(str(f) + "," + "M" + str(type) + "," + str(item[80:96]) + "," + "" + "," + str(parent) + "," + entityType + "," + string + "," + "" + "," + "" + "," + "" + "," + "" + "\n")


			atOffset += (blockSize - 0x18)

			amountOfItemLinks = 0
			for i in range(4):
				itemLink = scanForAdress(file, atOffset)
				if(itemLink != "null"):
					amountOfItemLinks = amountOfItemLinks + 1
					writeToJson(',\n' + '    "Linked data": ' + '[\n')
					readItemInfo(file, i, itemLink[0])
					if(i - 1) != 4:
						writeToJson(']\n')
					else:
						writeToJson('],\n')
				else:
					if i == 4:
						writeToJson('\n')
				atOffset += (blockSize - 0x48)
			writeToJson("\n},\n")


	if type == 1:
		blockSize = 0x4

		for f in range(numFiles):

			ID = readHexFromBIN1File(file, atOffset, 0x4)
			writeToJson('{\n')
			writeToJson('    "ID": "' + str(ID) + '"\n')
			writeToCSV("" + "," + "M" + str(type) + "," + "" + "," + "" + "," + str(ID) + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + "\n")
			if (f + 1) == numFiles:
				writeToJson('}\n')
			else:
				writeToJson('},\n')
			atOffset += 0x4

	if type > 1 and type < 5:
		blockSize = 0x38

		for f in range(numFiles):

			string1 = readStrFromOffset(file, atOffset + 0xC)
			string2 = readStrFromOffset(file, atOffset + 0x1C)
			value = readHexFromBIN1File(file, atOffset, 0x4)

			writeToJson('{\n')
			writeToJson('    "value": "' + value + '",\n')
			writeToJson('    "string": "' + string1 + '",\n')
			writeToJson('    "string_": "' + string2 + '"\n')
			writeToCSV("" + "," + "M" + str(type) + "," + "" + "," + "" + "," + "" + "," + "" + "," + string1 + "," + string2 + "," + "" + "," + "" + "," + value + "\n")
			if (f + 1) != numFiles:
				writeToJson('},\n')
			else:
				writeToJson('}\n')
			atOffset += blockSize


	if type == 5:
		blockSize = 0x20

		for f in range(numFiles):

			hash = readHexFromBIN1File(file, atOffset, 0x8)
			string = readStrFromOffset(file, atOffset + 0x18)

			writeToJson('{\n')
			writeToJson('    "hash": "' + hash + '",\n')
			writeToJson('    "string": "' + string + '"\n')
			writeToCSV("" + "," + "M" + str(type) + "," + hash + "," + "" + "," + "" + "," + "" + "," + string + "," + "" + "," + "" + "," + "" + "," + "" + "\n")
			if (f + 1) == numFiles:
				writeToJson('}\n')
			else:
				writeToJson('},\n')
			atOffset += blockSize


	if type == 6:
		blockSize = 0x0
		part = readPartFromBIN1File(file, fromOffset, fromOffset + 0x40)
		writeToJson("DATA TYPE 6 FOUND ======PLEASE REPORT THIS TO DEV=========\n")
		writeToJson("from: " + str(fromOffset) + "\n\n")
		print("  from: " + str(hex(fromOffset)) + ", to: " + str(hex(toOffset)))
		writeToJson("to: " + str(toOffset) + "\n\n")
		writeToJson(str(part) + "\n\n")
		print('\nDATA TYPE 6 FOUND\n\n\n')


	if type == 7:
		blockSize = 0x70

		for f in range(numFiles):

			hash = readHexFromBIN1File(file, atOffset, 0x8)
			string = readStrFromOffset(file, atOffset + 0x18)

			hash2 = readHexFromBIN1File(file, atOffset + 0x20, 0x8)
			string1 = readStrFromOffset(file, atOffset + 0x38)
			string2 = readStrFromOffset(file, atOffset + 0x48)
			string3 = readStrFromOffset(file, atOffset + 0x58)

			writeToJson('{\n')
			writeToJson('    "hash": "' + hash + '",\n')
			writeToJson('    "string": "' + string + '",\n')
			writeToJson('    "hash_": "' + hash2 + '",\n')
			writeToJson('    "string_": "' + string1 + '",\n')
			writeToJson('    "string__": "' + string2 + '",\n')
			writeToJson('    "string___": "' + string3 + '"\n')
			writeToCSV("" + "," + "M" + str(type) + "," + hash + "," + hash2 + "," + "" + "," + "" + "," + string + "," + string1 + "," + string2 + "," + string3 + "," + "" + "\n")
			if (f + 1) != numFiles:
				writeToJson('},\n')
			else:
				writeToJson('}\n')
			atOffset += blockSize

	if atOffset - blockSize > toOffset:
		print("WARNING, THE END WAS AT: " + str(hex(toOffset)) + " AND THE FILE READ UNTIL " + str(hex(atOffset)))

def readItemInfo(file, type, fromOffset):
	atOffset = fromOffset

	numFiles = readHexFromBIN1File(file, atOffset, 0x4)
	numFiles = int(numFiles, 16)
	atOffset += 0x4
	print("     sub-Type " + str(type) + " found")
	print("     amount: " + str(numFiles))

	if type == 3:

		for f in range(numFiles):
			itemLink = readPartFromBIN1File(file, atOffset, atOffset + 0x10)
			string = readStrFromOffset(file, atOffset + 0x8)
			writeToJson('{\n')
			atOffset += 0x10
			subItemLink = scanForAdress(file, atOffset)
			if(subItemLink != "null"):
				writeToJson('\n' + '    "' + string + '": [\n')
				writeToCSV("" + "," + "S" + str(type) + "," + "" + "," + "" + "," + "" + "," + "" + "," + string + "," + "" + "," + "" + "," + "" + "," + "" + "\n")
				readSubItemInfo(file, type, subItemLink[0])

				writeToJson(']\n')
			if (f + 1) == numFiles:
				writeToJson('}\n')
			else:
				writeToJson('},\n')

				atOffset += 0x18

	if type == 0:

		for f in range(numFiles):
			string1 = readStrFromOffset(file, atOffset + 0x8)
			string2 = readStrFromOffset(file, atOffset + 0x20)
			value = readHexFromBIN1File(file, atOffset + 0x10, 0x4)
			writeToJson('{\n')
			writeToJson('    "string": "' + string1 + '",\n')
			writeToJson('    "string_": "' + string2 + '",\n')
			writeToJson('    "value": "' + str(value) + '"\n')
			writeToCSV("" + "," + "S" + str(type) + "," + "" + "," + "" + "," + "" + "," + "" + "," + string1 + "," + string2 + "," + "" + "," + "" + "," + str(value) + "\n")

			if (f + 1) == numFiles:
				writeToJson('}\n')
			else:
				writeToJson('},\n')

			atOffset += 0x28

	if type == 2:

		for f in range(numFiles):
			string = readStrFromOffset(file, atOffset + 0x8)
			value = readHexFromBIN1File(file, atOffset + 0x10, 0x4)
			writeToJson('{\n')
			writeToJson('    "string": "' + string + '",\n')
			writeToJson('    "value": "' + str(value) + '"\n')
			writeToCSV("" + "," + "S" + str(type) + "," + "" + "," + "" + "," + "" + "," + "" + "," + string + "," + "" + "," + "" + "," + "" + "," + str(value) + "\n")

			if (f + 1) == numFiles:
				writeToJson('}\n')
			else:
				writeToJson('},\n')

			atOffset += 0x18

	if type == 1:

		for f in range(numFiles):
			string = readStrFromOffset(file, atOffset + 0x8)
			varType = readHexFromBIN1File(file, atOffset + 0x10, 0x4)
			varType = int(varType, 16)
			writeToJson('{\n')
			writeToJson('    "type": "' + str(varType) + '",\n')
			if scanForAdress(file, atOffset + 0x18) != "null":
				writeToJson('    "string": "' + string + '",\n')
			else:
				writeToJson('    "string": "' + string + '"\n')
			writeToCSV("" + "," + "S" + str(type) + "," + "" + "," + "" + "," + "" + "," + str(varType) + "," + string + "," + "" + "," + "" + "," + "" + "," + "" + "\n")

			atOffset += 0x18
			subItemLink = scanForAdress(file, atOffset)
			if(subItemLink != "null"):
				writeToJson('\n' + '    "instances": [\n')
				readSubItemInfo(file, type, subItemLink[0])
				writeToJson(']\n')

			if (f + 1) == numFiles:
				writeToJson('}\n')
			else:
				writeToJson('},\n')

				atOffset += 0x18

def readSubItemInfo(file, itemType, fromOffset):

	atOffset = fromOffset
	numFiles = readHexFromBIN1File(file, atOffset, 0x4)
	numFiles = int(numFiles, 16)
	atOffset += 0x4
	print("       instances found")
	print("       amount: " + str(numFiles))

	if itemType != 1:
		for f in range(numFiles):
			subItemLink = readPartFromBIN1File(file, atOffset, atOffset + 0x4)
			writeToJson('          "' + str(subItemLink))
			if (f + 1) != numFiles:
				writeToJson('",\n')
			else:
				writeToJson('"\n')
			atOffset += 0x4

	if itemType == 1:
		for f in range(numFiles):
			ID = readHexFromBIN1File(file, atOffset + 0xC, 0x4)
			string = readStrFromOffset(file, atOffset + 0x18)
			if len(string) > 0:
				writeToJson('          "' + str(ID) + " = " + string)
				writeToCSV("" + "," + "SS" + str(itemType) + "," + "" + "," + "" + "," + str(ID) + "," + "" + "," + string + "," + "" + "," + "" + "," + "" + "," + "" + "\n")
			else:
				writeToJson('          "' + str(ID))
				writeToCSV("" + "," + "SS" + str(itemType) + "," + "" + "," + "" + "," + str(ID) + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + "\n")
			if (f + 1) != numFiles:
				writeToJson('",\n')
			else:
				writeToJson('"\n')

			atOffset += 0x20


if len(sys.argv) == 3:
	filePathBIN1 = sys.argv[1]
	try:
		if sys.argv[2] == "JSON" or sys.argv[2] == "BOTH":
			open(filePathBIN1 + '.BIN1decoded.JSON', 'w').close()
		if sys.argv[2] == "CSV" or sys.argv[2] == "BOTH":
			open(filePathBIN1 + '.BIN1decoded.csv', 'w').close()
	except:
		print("Tried to clear existing file, but an error occured.")
		print("Maybe the file is still open in another window?")
	with open(filePathBIN1,'rb') as file:
		try:
			if isBIN1File(file):
				print(filePathBIN1)
				if sys.argv[2] == "CSV":
                                	writeToCSV("index, Block, hash, hash_, Parent, Type, String, String_, String__, String___, Value\n")
				writeToJson("[\n")
				#skips header so start at 0x18
				atOffset = 0x18
				for i in range(8):
					adress = scanForAdress(file, atOffset)
					atOffset += 0x18
					if(adress != "null"):
						readData(file, i, adress[0], adress[1])
				writeToJson("]\n")
				connectJsonParts()
		except Exception as e:
			print(e)
else:
	print('Usage: python(3) bin1decode.py <path to BIN1 file> <JSON, CSV or BOTH>')
