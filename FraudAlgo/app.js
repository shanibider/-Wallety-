const http = require('http');
const url = require('url');
const fs = require('fs');
const csv = require('csv-parser');

const hostname = '127.0.0.1';
const port = 3000;

function isFraudManyInOneHour(transactions) {
    let prevTimestamp = null;
    let numTransactions = 0;
    const HOUR_IN_MS = 60 * 60 * 1000;
    transactions.forEach(data => {
        const timestamp = data['Time'].getTime();
        if (prevTimestamp === null) {
            prevTimestamp = timestamp;
        }

        if (timestamp - prevTimestamp < HOUR_IN_MS) {
            numTransactions++;
        } else {
            numTransactions = 1;
            prevTimestamp = timestamp;
        }

        if (numTransactions >= 3) {
            data['isFraud'] = true;
            data['fraudReason'] = "Too many transactions in one hour";
        }
    });

}

async function getTransactions(userId) {
    let transactions = [];

    // read the CSV file and parse its data
    await new Promise((resolve, reject) => {
        fs.createReadStream('data.csv')
            .pipe(csv())
            .on('data', (data) => {

                if (userId && data['User ID'] !== userId.toString()) {
                    return;
                }
                data['Time'] = new Date(data['Time']);
                transactions.push(data);
            })
            .on('end', () => {
                resolve();
            })
            .on('error', (error) => {
                reject(error.message);
            });
    });

    transactions.forEach(data => {
        data['isFraud'] = false;
        data['fraudReason'] = "Legit";
    });


    isFraudManyInOneHour(transactions);

    return transactions;
}

const server = http.createServer((req, res) => {
    const parsedUrl = url.parse(req.url, true);
    const userId = parsedUrl.query && parsedUrl.query.userId;

    res.statusCode = 200;
    res.setHeader('Content-Type', 'text/plain');

    (async function () {
        try {
            let transactions = await getTransactions(userId);
            res.end(JSON.stringify(transactions));
        } catch (error) {
            console.error(error);
        }
    })();

});

server.listen(port, hostname, () => {
    console.log(`Server running at http://${hostname}:${port}/`);
});