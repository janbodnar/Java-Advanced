var getXML2 = (url, callback) => {

    let xhr = new XMLHttpRequest();
    xhr.open('GET', url, true);
    xhr.responseType = 'xml';

    xhr.onload = () => {

        let status = xhr.status;

        if (status == 200) {
            callback(null, xhr.responseXML);
        } else {
            callback(status);
        }
    };

    xhr.send();
};

getXML2('http://webcode.me/users.xml', (err, xml) => {

    if (err != null) {
        console.error(err);
    } else {

        let users = xml.getElementsByTagName("user");

        for (let i = 0; i < users.length; i++) {

            let value = '';

            for (let j = 0; j < users[i].children.length; j++) {
                value += " " + users[i].children[j].innerHTML;
            }

            let node = document.createElement("li");
            let textnode = document.createTextNode(value);

            node.appendChild(textnode);
            document.getElementById("output").appendChild(node);
        }
    }
});
