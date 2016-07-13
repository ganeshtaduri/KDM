import wikipedia
data=wikipedia.page("Republican Party (United States)")
file = open('output_wiki_RepublicanParty.txt', 'w',encoding='utf8')
file.write(data.content)
file.close()

