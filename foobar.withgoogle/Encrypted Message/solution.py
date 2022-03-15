import base64

encrypted = "AkgGBBYCCxdBFllVVVYSEwsFRhZVT1ISGg0CAVNWDApSUU9BSQFBRRwKGBQRRkJEFVQfCRoDARJJ RAgRXgYbEgcECg1QXRxIWVFSAA0MW1QPChgUGxVJRAgRXhobHRoCBQFWFlVPUgMUAwwNRkJeT09R UhIPAlcWVU9SFxoOSUQIEV4YHB9URhM="
key = str.encode("youquand21")

decoded = base64.b64decode(encrypted)

decrypted = ""

for i in range(len(decoded)):
    decrypted += chr(key[i % len(key)] ^ decoded[i])
    
print(decrypted)