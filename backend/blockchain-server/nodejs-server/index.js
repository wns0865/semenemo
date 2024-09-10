const express = require('express');
const app = express();
const cors = require('cors');

app.use(cors());

app.get('/', function (req, res) {
  return res.json({
    message: 'hello world',
    status: 'success'
  });
});

app.listen(3000, function () {
  console.log('server listening on port 3000');
});