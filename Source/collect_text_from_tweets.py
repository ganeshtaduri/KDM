import json
import sys
tweets_data_path = 'E:\\Twitter data\\tweets.json'
tweets =[]
for line in open(tweets_data_path):
	try:
		tweets.append(json.loads(line))
	except:
		pass

for tweet in tweets:
	try:
		print (tweet['text'])
	except:
		pass
