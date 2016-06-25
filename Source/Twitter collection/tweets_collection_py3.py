from __future__ import absolute_import, print_function

from tweepy.streaming import StreamListener
from tweepy import OAuthHandler
from tweepy import Stream


access_token = "1145809453-ctTAQZdvp4E6lFlyuFxU21Ke01cxfr3niEz1VB5"
access_token_secret = "XEfQKspCOz47X1cC1DzBAXKHynLjdV1hlP3KyjaaBA6Vt"
consumer_key = "REMK0Pi0Ioz6hhlTp0o9AMCss"
consumer_secret = "jEXf0OzqGL8ah7YwQ2f5dgE6OA438mXND74vquVOyoI0CkWWl8"

class StdOutListener(StreamListener):
    """ A listener handles tweets that are received from the stream.
    This is a basic listener that just prints received tweets to stdout.
    """
    def on_data(self, data):
        file = open('out.json', 'w')
        file.write(data)
        print (data)
        return True

    def on_error(self, status):
        print(status)

if __name__ == '__main__':
    l = StdOutListener()
    auth = OAuthHandler(consumer_key, consumer_secret)
    auth.set_access_token(access_token, access_token_secret)

    stream = Stream(auth, l)
    stream.filter(track=['Election 2016'])
