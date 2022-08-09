# Web crawler

**Overview**:

For the purposes of this project, we define
- the internet as the test JSON file included,
- a web crawler as software that requests pages from the internet, parses the content to extract all the links in the page, and visits the links to crawl those pages, to an infinite depth.

## Tests

To test this software, you can run it as spring boot application, using the url http://localhost:8080/challenge/pages/search/{address} , or you can run the tests WebCrawlerServiceTest , on shouldSuccessGetInformation method , use the address you want, and check the results on the console.
