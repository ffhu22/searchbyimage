# searchbyimage
A Selenium Java testing project for searching by image

Env:
- Chrome Version 81.0.4044.138 
- Selenium Java

Test Case: Search an image from baidu, verify the results list, and visit one result page.

Test Steps:
1. Open website "www.baidu.com"
2. Click "Search By Image" icon, load a local image "Golden_Gate_Bridge.jpg" from basedir/baseimage
3. Read the pre-defined image related keywords from a configuration file basedir/config/GoldenGateBridge.properties
4. In the results list page, verify that each result description contains the pre-defined image related keywords
5. Read the number of which result to visit from basedir/config/config.properties
6. Visit the result per the number
7. Take a screenshot of the last visited page and store it in basedir/screenshots
