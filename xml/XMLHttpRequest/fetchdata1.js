var getXML = (url, callback) => {

    let xhr = new XMLHttpRequest();
    xhr.open('GET', url, true);
    xhr.responseType = 'xml';

    xhr.onload = () => {

        let status = xhr.status;

        if (status == 200) {
            callback(null, xhr.response);
        } else {
            callback(status);
        }
    };

    xhr.send();
};

getXML('http://webcode.me/users.xml',  (err, data) => {

    if (err != null) {
        console.error(err);
    } else {

        console.log(data);
    }
});
