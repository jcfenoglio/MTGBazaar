import csv
import json
import pandas as pd
import numpy as np

def parseList(mList):
    if mList == "[]" :
        return [""]
    returnList = []
    splitList = mList.split("\'")
    for item in splitList:
        if item != '[' and item != ']' and item != ', ':
            returnList.append(item.split("\'")[0])
    return returnList

# pull in all the cards and parse it into a CSV, since CSVs are easier to import into pandas
try:
    x = open('input/AllCards.json', encoding='utf-8')
    parsed = json.load(x)
    myFile = open('temp/AllCards.csv','w', encoding='utf-8')
    writer = csv.writer(myFile)
    data = ["name", "manaCost", "cmc", "color", "type", "supertype", "subtype", "text", "power", "toughness"]
    writer.writerow(data)

    for x in parsed.keys():
        keys = parsed[x].keys()
        
        name = parsed[x]['name']
        name = name.replace(".", "%2E")
        
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

    myFile.close()

    # Pull in and parse all the se
    allc = open('input/AllSets.json', encoding='utf-8')
    allparsed = json.load(allc)

    myFile = open('temp/AllSets.csv','w', encoding='utf-8')
    writer = csv.writer(myFile)
    data = ["name", "manaCost", "cmc", "color", "type", "supertype", "subtype", "text", "power", "toughness","rarity", "set"]
    writer.writerow(data)

    for x in allparsed.keys():
        cards = allparsed[x]['cards']
        sets = x
        for y in cards:
            keys = y.keys()
            name = y['name']
            name = name.replace(".", "%2E")
        
            if "manaCost" not in keys:
                manacost = ""
            else:
                manacost = y['manaCost']
            
            if "cmc" not in keys:
                cmc = 0
            else:
                cmc = y['cmc']
            
            if "types" not in keys:
                types = "[]"
            else:    
                types = y['types']
            
            if "subtypes" not in keys:
                subtypes = "[]"
            else:
                subtypes = y['subtypes']
            
            if "supertypes" not in keys:
                supertypes = "[]"
            else:
                supertypes = y['supertypes']
        
            if "text" not in keys:
                text = ""
            else:
                text = y['text']
            
            if "power" not in keys:
                power = -1
            else:
                if y["power"] == "*":
                    power = -2
                else:
                    power = y["power"]
        
            if "toughness" not in keys:
                toughness = -1
            else:
                if y["toughness"] == "*":
                    toughness = -2
                else:
                    toughness = y["toughness"]
            if "colors" not in keys:
                color = "['Colorless']"
            else:
                color = y["colors"]
                
            if "rarity" not in keys:
                rarity = ""
            else:
                rarity = y['rarity']
            data = [name,manacost,cmc,color,types,supertypes,subtypes,text,power,toughness, rarity,sets]
            writer.writerow(data)

    myFile.close()
except:
    print("this script requires AllSets.json and AllCards.json to be in a subfolder called './input'.\nThese files can be found at mtgjson.com")
    raise()

df = pd.read_csv('temp/AllCards.csv', encoding='utf-8')
df1 = pd.read_csv('temp/AllSets.csv', encoding='utf-8')

df['sets'] = ""
df['rarity'] = ""

total = df1.shape[0]
print('total number of rows:', total)

# loop through and get the set of a card.  if card already has something it its set column,
# add the new set to the list
for index, row in df1.iterrows():
    
    if(index % 2000 == 0):
        print('percentage done:', round(index/total, 3) * 100, '%')
   
    sets = df.iloc[df.index[df['name'] == row['name']]]['sets']

    rarity = df.iloc[df.index[df['name'] == row['name']]]['rarity']
    if(rarity.any() == ""):
        rarity = row["rarity"]
    else:
        rarity = rarity + "," + row["rarity"]
    if(sets.any() == ""):
        sets = row["set"]
    else:
        sets = sets + "," + row["set"]

    df.set_value(df.index[df['name'] == row["name"]],"sets",sets)
    df.set_value(df.index[df['name'] == row["name"]],"rarity",rarity)

print('PANDAS DONE')
df = df.set_index('name')
df.to_json(path_or_buf='./output/ToEdit.json', orient='index')

x = open('output/ToEdit.json', encoding='utf-8')
finished = json.load(x)
for x in finished.keys():
    finished[x]['type'] = parseList(finished[x]['type'])
    finished[x]['supertype'] = parseList(finished[x]['supertype'])
    finished[x]['subtype'] = parseList(finished[x]['subtype'])
    finished[x]['color'] = parseList(finished[x]['color'])
    finished[x]['sets'] = finished[x]['sets'].split(',')
    finished[x]['rarity'] = finished[x]['rarity'].split(',')

with open('output/ToUpload.json', 'w', encoding = 'utf-8') as out:
    json.dump(finished, out)
