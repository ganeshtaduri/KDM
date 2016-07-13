import wikipedia
data=wikipedia.page("United States presidential election, 2016")
file = open('output_wiki.txt', 'w')
file.write(data.content)
file.close()

