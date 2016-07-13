import wikipedia
data=wikipedia.page("Vice President of the United States")
file = open('output_wiki_vicepresident.txt', 'w')
file.write(data.content)
file.close()

