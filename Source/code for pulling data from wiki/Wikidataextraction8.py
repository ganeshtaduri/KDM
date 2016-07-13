import wikipedia
data=wikipedia.page("Hillary Clinton")
file = open('output_wiki_HillaryClinton.txt', 'w',encoding='utf8')
file.write(data.content)
file.close()

