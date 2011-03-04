import java.net.*;
import java.io.*;

/*
http://www.json.org/javadoc/org/json/JSONObject.html

http://blogs.msdn.com/b/webngram/archive/2010/11/08/using-the-microsoftngram-python-module.aspx

>>> s = MicrosoftNgram.LookupService()
>>> s.GetModel()
'bing-body/jun09/3'
>>> s = MicrosoftNgram.LookupService(token='xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx')
>>> s.GetModel()
'bing-body/jun09/3'
>>> s = MicrosoftNgram.LookupService('xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx')
>>> s.GetModel()
'bing-body/jun09/3'
>>> s = MicrosoftNgram.LookupService(token='xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx',model='bing-body/jun09/3')
>>> s.GetModel()
'bing-body/jun09/3'
>>> s = MicrosoftNgram.LookupService('xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx','bing-body/jun09/3')
>>> s.GetModel()
'bing-body/jun09/3'

>>> s = MicrosoftNgram.LookupService(model='bing-body/apr10/5')
>>> s.GetConditionalProbability('happy cat is happy')
-0.93900499999999998
>>> s.GetConditionalProbability('happy cat is sad')
-4.2167089999999998
>>> s.GetJointProbability('kthxbai')
-7.6080370000000004

>>> for t in s.Generate('happy cat is', maxgen=5): print t
...
('always', -0.36325089999999999)
('a', -0.89422170000000001)
('happy', -0.93900499999999998)

~~~~~~~~~~~~~~~~~~~~~~~~~~ REST API:	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

A GET call on this URI will return a list of models supported in path-form which can be used in the various lookup methods:

http://web-ngram.research.microsoft.com/rest/lookup.svc/{catalog}/{version}/{order}/{operation}?{parameters}

Operation 	Verb (and SOAP equivalent) 	Parameters 	Required?
jp 	GET = GetProbability
	POST = GetProbabilities 	u=<user_token> 	Yes
								p=<phrase> 	GET only
								format=<format> 	No

cp 	GET = GetConditionalProbability
	POST = GetConditionalProbabilities 	u=<user_token> 	Yes
										p=<phrase> 	GET only
										format=<format> 	No

gen 	GET = Generate 	u=<user_token> 	Yes
						p=<phrase> 	Yes
						n=<max tokens> 	Yes
						cookie=<cookie> 	Yes, except first call
						format=<format> 	No

In each case, the format can be one of the following: text, json, or xml. When no format is specified, text is assumed.

When using the batch-mode methods (i.e. with a POST request), each phrase should be separated by a newline character.

*/

public class URLConnectionReader {
    public static void main(String[] args) throws Exception {
		String user_token = "u=6855f2b9-927e-4a6e-8766-fe907b235186&";
		String phrase = "p=happy";	// cat is happy";
		//String phrase = "p=happy cat is happy";
		String catalog = "bing-body/apr10/5/";
		String myconst = "http://web-ngram.research.microsoft.com/rest/lookup.svc/";
		String [] opid = {"jp", "cp", "gen"};

		String myurl = myconst + catalog + opid[1] + '?' + user_token + phrase;
		System.out.println (myurl);

		URL yahoo = new URL(myurl);
		//URL yahoo = new URL("http://web-ngram.research.microsoft.com/rest/lookup.svc/?format=xml");
        //URL yahoo = new URL("http://web-ngram.research.microsoft.com/rest/lookup.svc/?format=json");
        //URL yahoo = new URL("http://www.yahoo.com/");
        URLConnection yc = yahoo.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                yc.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null) 
            System.out.println(inputLine);

//http://web-ngram.research.microsoft.com/rest/lookup.svc/bing-body/apr10/5/cp?u=6855f2b9-927e-4a6e-8766-fe907b235186&p=happy%20cat

//http://web-ngram.research.microsoft.com/rest/lookup.svc/bing-body/apr10/5/cp?u=6855f2b9-927e-4a6e-8766-fe907b235186&p=happy cat is happy

        in.close();
    }
}
