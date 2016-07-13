import wikipedia
data=wikipedia.page("United States presidential election")
file = open('output_wiki_uspreselection.txt', 'w')
file.write(data.content)
file.close()

