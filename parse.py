import csv
import json
import pandas as pd
import numpy as np

cardJson = open('./AllCards.json')
parsed = json.load(cardJson)

myFile = open('./AllCards.csv','w')
writer = csv.writer(myFile)
data = ["name", "manaCost", "cmc", "color", "type", "supertype", "subtype", "text", "power", "toughness","rarity", "set"]
writer.writerow(data)

for x in parsed.keys():
    keys = parsed[x].keys()
    
    name = parsed[x]['name']
    
    if "manaCost" not in keys:
        manacost = ""
    else:
        manacost = parsed[x]['manaCost']
        
    if "cmc" not in keys:
        cmc = 0
    else:
        cmc = parsed[x]['cmc']
        
    if "types" not in keys:
        types = "[]"
    else:    
        types = parsed[x]['types']
        
    if "subtypes" not in keys:
        subtypes = "[]"
    else:
        subtypes = parsed[x]['subtypes']
        
    if "supertypes" not in keys:
        supertypes = "[]"
    else:
        supertypes = parsed[x]['supertypes']
    
    if "text" not in keys:
        text = ""
    else:
        text = parsed[x]['text']
        
    if "power" not in keys:
        power = -1
    else:
        if parsed[x]["power"] == "*":
            power = -2
        else:
            power = parsed[x]["power"]
                
    if "toughness" not in keys:
        toughness = -1
    else:
        if parsed[x]["toughness"] == "*":
            toughness = -2
        else:
             toughness = parsed[x]["toughness"]
    if "colors" not in keys:
         color = "['Colorless']"
    else:
        color = parsed[x]["colors"]
    data = [name,manacost,cmc,color,types,supertypes,subtypes,text,power,toughness]
    writer.writerow(data)

    df = pd.read_csv('./AllCards.csv')

    df.to_json(path_or_buf='./ToUpload.json', orient='index')