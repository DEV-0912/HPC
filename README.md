# HPC Repository

# To install gcc in linux
sudo apt install gcc

# To install VS-code in linux
sudo apt install code 

# To create a new folder 
mkdir foldername 

# To create a file or to modify the code in the file 
# (check the filename correctly if you enter the name wrong it will create newfile )
Method 1: vi filename.c 
Method 2: nano filename.c

# To run the file and create the exe file
gcc .\filename.c -o filename -fopenmp

# To get the ouput
./filename