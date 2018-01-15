import concurrent.futures
import os
import shutil
import subprocess
import sys

# Constants
SOURCE_FOLDERS_COUNT = 29
DECOMPILER = "cfr_0_124.jar"

def main():	
	# Get paths
	rootPath = GetPath(__file__, 2)
	compPath = rootPath + "\\compiled"
	cfrPath = rootPath + "\\tools\\" + DECOMPILER
	
	CheckForProblems(compPath)
	
	# Walk compiled directory and build an iterable to map to subprocesses
	# Copy any non-class files
	filesToDecompile = []
	for dirPath, dirNames, fileNames in os.walk(compPath):
		for fileName in fileNames:
			if fileName == ".placeholder":
				continue
			
			filePath = dirPath + "\\" + fileName
			if ".class" in fileName:
				filesToDecompile.append(filePath)
			else:
				CopyFile(filePath)
				
	# Decompile each file in filesToDecompile in subprocesses
	with concurrent.futures.ProcessPoolExecutor() as executor:
		for fileName in executor.map(Decompile, filesToDecompile, [cfrPath]*len(filesToDecompile)):
			print('Decompiled ' + fileName)
					
	print("Decompilation complete");		

# GetPath - 
def GetPath(path, up):
	return "\\".join(os.path.abspath(path).split("\\")[0:(0-up)])

# CheckForProblems -	
def CheckForProblems(compPath):
	if len(os.listdir(compPath)) != SOURCE_FOLDERS_COUNT:
		print("compiled does not have the correct number of folders")
		sys.exit()

# Decompile -
def Decompile(filePath, cfrPath):
	# Decompile
	decompiled = subprocess.check_output(["java", "-jar", cfrPath, "--comments", "false", "--showversion", "false", filePath])
	
	# Output to decompiled java file
	outPath = filePath.replace("\\compiled\\", "\\decompiled\\").replace(".class", ".java")
	os.makedirs(os.path.dirname(outPath), exist_ok=True)
	
	outFile = open(outPath, "w+")
	outFile.write(decompiled.decode("utf-8"))
	outFile.close()
	
	return filePath.split("\\")[-1]

# CopyFile - 
def CopyFile(filePath):
	outPath = filePath.replace("\\compiled\\", "\\decompiled\\")
	os.makedirs(os.path.dirname(outPath), exist_ok=True)
	shutil.copy2(filePath, outPath)
	
	fileName = filePath.split("\\")[-1]
	print("Copied " + fileName)
	
if __name__ == "__main__":
	main()