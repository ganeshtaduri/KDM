import wikipedia
data=wikipedia.page("Barack Obama")
file = open('output_wiki_barackObama.txt', 'w',encoding='utf8')
file.write(data.content)
file.close()

