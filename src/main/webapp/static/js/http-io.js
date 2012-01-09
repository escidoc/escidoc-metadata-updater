function send() {
	var xhr = new XMLHttpRequest();
	xhr.open('GET', getUri(), false);
	xhr.setRequestHeader('Accept', 'application/xml;charset=UTF-8');
	xhr.setRequestHeader('Content-Type', 'application/xml;charset=UTF-8');

	xhr.onreadystatechange = function(event) {
		if (xhr.readyState === 4) {
			if (xhr.status === 200) {
				var r = xhr.responseText;
				console.log(r);
				console.log('uri' + getUri());

				var rXml = xhr.responseXML;
				;
				console.log(rXml);

				console.log(document.getElementsByTagName('input'));
				var list = document.getElementsByTagName('input');

				Array.prototype.forEach.call(list,
						function(li, index, nodeList) {
							if (li.type !== 'button') {
								if (li.defaultValue !== li.value) {
									var found = rXml
											.getElementsByTagName(li.name);
									var input = found[0];
									input.textContent = li.value;
								}
							}
						});
				put(rXml);
			} else {
				console.log("status: ", xhr2.statusText);
			}
		}
	};
	xhr.send(null);

	return false;
}

function put(xml) {
	var xhr2 = new XMLHttpRequest();
	xhr2.open('PUT', getUri(), false);
	xhr2.setRequestHeader('Accept', 'application/xml;charset=UTF-8');
	xhr2.setRequestHeader('Content-Type', 'application/xml;charset=UTF-8');

	xhr2.onreadystatechange = function(event) {
		if (xhr2.readyState === 4) {
			if (xhr2.status === 200) {
				var r = xhr2.responseXML;
				console.log("Successfully update the metadata.");
				console.log(r);
				alert('The response was: ' + xhr2.status + ', '
						+ xhr2.responseText);
			} else if (xhr2.status === 303) {
				alert('redirect');
			} else if (xhr2.status === 0) {
				alert('zero...');
			} else {
				console.log("status: ", xhr2.statusText);
				alert('Snap, something went wrong: ' + xhr2.status
						+ ', reason: ' + xhr2.responseText);
			}
		}
	};
	xhr2.send(xml);
}

function getUri() {
	return window.location.href;
}