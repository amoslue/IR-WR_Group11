A Note to the User

The material on this disk is copyrighted and is subject to the terms and 
conditions of the TREC Information-Retrieval Text Research Collection User 
Agreement, which must be signed in order to obtain a copy of the CD-ROM on 
which this data is to be found.

This data was obtained from Lexis-Nexis with permission of The Los Angeles
Times.  The data represents a sampling of approximately 40% of the
articles published by the Los Angeles Times in the two year period
from Jan 1, 1989 -- December 31, 1990.  Our thanks to Lexis-Nexis and
the Los Angeles Times for providing the data. 

The changes between the original material as it came from the publisher and the
version on this disk is detailed in the following file: readchg

The format uses a labeled bracketing, expressed in the style of SGML (Standard 
Generalized Markup Language).  The SGML DTD used for verification at NIST is 
included on the CD.  The philosophy in the formatting at NIST has been
to preserve as much of the original structure as possible, but to provide
enough consistency to allow simple decoding of the data.

A sample document from the data is shown below:

<DOC>
<DOCNO> LA042990-0001 </DOCNO>
<DOCID> 211062 </DOCID>
<DATE>
<P>
April 29, 1990, Sunday, Home Edition 
</P>
</DATE>
<SECTION>
<P>
TV Times; Page 3; Television Desk 
</P>
</SECTION>
<LENGTH>
<P>
54 words 
</P>
</LENGTH>
<TEXT>
<P>
What a magnificent production of "Jesus of Nazareth." Hats off to Zeffirelli 
(et al.) who had the vision for such a masterpiece. 
</P>
<P>
It was my fifth viewing of the miniseries, which I would never miss. We seldom 
see such quality in films these days and what a treat to have NBC bring it to 
us. 
</P>
<P>
Mrs. D. Porter, Yucaipa 
</P>
</TEXT>
<TYPE>
<P>
Letter to the Editor 
</P>
</TYPE>
</DOC>

Every document is bracketed by <DOC> </DOC> tags and has a unique document 
number, bracketed by <DOCNO> </DOCNO> tags.
The set of tags includes the following (with corresponding end tags,
see the DTD for more details):
        <TEXT>
        <BYLINE>
        <CORRECTION>
        <CORRECTION-DATE>
        <DATE>
        <DATELINE>
        <DOC>
        <DOCNO>
        <DOCID>
        <GRAPHIC>
        <HEADLINE>
        <LENGTH>
        <SECTION>
        <SUBJECT>
        <TYPE>
        <CELLRULE>
        <P>
        <ROWRULE>
        <TABLE ...>
        <TABLECELL ...>
        <TABLEROW>

The dataset has been compressed using the UNIX "compress" utility.
The set of articles from a given date are included in one file
named for that date.  Thus, the file "la042990" contains articles
published on April 29, 1990.

Both as part of the philosophy of leaving the data as close to the original as 
possible, and because it is impossible to check all the data manually, there 
are many "errors" in the data.  These range from errors in the original data, 
such as typographical errors, improper tagging errors, to errors in the 
reformatting done at NIST.  The error-checking has concentrated on allowing 
readability of the data rather than on correcting content.  This means that 
there have been automated checks for control characters, special symbols,
foreign language characters, for correct matching of the begin and end tags, 
and for complete DOC and DOCNO fields.  The types of "errors" remaining include 
fragment sentences, strange formatting around tables or other "non-textual" 
items, misspellings, missing fields (that are generally missing from the data), 
etc.

