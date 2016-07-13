import wikipedia
data=wikipedia.page("Democratic Party (United States)")
file = open('output_wiki_democraticParty.txt', 'w',encoding='utf8')
file.write(data.content)
file.close()

