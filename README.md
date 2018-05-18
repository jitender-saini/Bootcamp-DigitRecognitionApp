# Digit Recognition Application
test

This repository contains the Code and Data Files relevant to our Bootcamp
Digit Recognition App Project.

To run this code follow the below given instructions:

<h3>To train the model:</h3>

1) In terminal, navigate to the model directory and execute "python model.py". This will train the model as well as test it on sample test data.

<h3>To predict with this model:</h3>
<ol>
<li>Run the api with "python demo.py"</li>
<li>In terminal you will be shown a url at which the api will be running, Let that url be X.</li>
<li>At X/model/predict. Send a POST request using Postman.</li>
<li>While sending the post request, make sure you send an image as a part of the request. To do this Select "Body" tab and in
"form-data" category, under key column write "test_image" and select an appropriate image using file selector under value column.</li>
<li>The result is in json format.</li>
</ol>
