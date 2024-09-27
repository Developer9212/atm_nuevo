import time

file_path = '/home/atms_config.txt'

def read_file():
    with open(file_path, 'r') as file:
        content = file.read()
        print(content)

while True:
    read_file()
    time.sleep(5)  # Espera 5 segundos antes de volver a leer el archivo
