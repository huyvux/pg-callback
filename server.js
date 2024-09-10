const express = require('express');
const app = express();
const bodyParser = require('body-parser');

// Middleware to parse JSON bodies and URL-encoded data
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// Route handler for GET and POST requests
app.all('/api/public/withdraw/:paymentChannel', (req, res) => {
  const paymentChannel = req.params.paymentChannel;

  // Log headers and body information
  console.log("Header:");
  console.log(getHeadersInfo(paymentChannel, req));
  console.log("Body:");
  console.log(getBodyInfo(paymentChannel, req));

  res.status(200).send("success");
});

// Function to extract headers
function getHeadersInfo(paymentGateway, req) {
  try {
    let headersMap = {};
    for (let key in req.headers) {
      headersMap[key] = req.headers[key];
    }
    return headersMap;
  } catch (err) {
    return {};
  }
}

// Function to extract body parameters
function getBodyInfo(paymentGateway, req) {
  try {
    let bodyMap = {};

    // Get parameters from query or body
    for (let key in req.body) {
      bodyMap[key] = req.body[key];
    }

    for (let key in req.query) {
      bodyMap[key] = req.query[key];
    }

    return bodyMap;
  } catch (err) {
    return {};
  }
}

// Start the server
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});
