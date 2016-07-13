import wikipedia
data=wikipedia.page("Donald Trump")
file = open('output_wiki_DonaldTrump.txt', 'w',encoding='utf8')
file.write(data.content)
file.close()

