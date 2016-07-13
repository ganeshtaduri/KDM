import wikipedia
data=wikipedia.page("President of the United States")
file = open('output_wiki_president.txt', 'w')
file.write(data.content)
file.close()

