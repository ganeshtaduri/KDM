import wikipedia
data=wikipedia.page("United States")
file = open('output_wiki_us.txt', 'w',encoding='utf8')
file.write(data.content)
file.close()

