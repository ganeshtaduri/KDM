import wikipedia
data=wikipedia.page("Electoral College (United States)")
file = open('output_wiki_electoralcollege.txt', 'w',encoding='utf8')
file.write(data.content)
file.close()

