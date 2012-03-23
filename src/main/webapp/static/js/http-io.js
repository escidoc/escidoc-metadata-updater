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
        console.log(rXml);

        console.log(document.getElementsByTagName('input'));
        var list = document.getElementsByTagName('input');

        Array.prototype.forEach.call(list, function(li, index, nodeList) {
          if (li.type !== 'button') {
            if (li.defaultValue !== li.value) {
              if (li.name != null) {
                console.log("li: " + li.name);
                if (rXml != null) {
                  var found = rXml.getElementsByTagName(li.name);
                  if (found != null && found.length > 0) {
                    var input = found[0];
                    input.textContent = li.value;
                  }
                }
              }
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
        alert('The response was: ' + xhr2.status + ', ' + xhr2.responseText);
      } else if (xhr2.status === 303) {
        alert('redirect');
      } else if (xhr2.status === 0) {
        alert('zero...');
      } else {
        console.log("status: ", xhr2.statusText);
        alert('Snap, something went wrong: ' + xhr2.status + ', reason: '
            + xhr2.responseText);
      }
    }
  };
  xhr2.send(xml);
}

function getUri() {
  return window.location.href;
}

function sendRawXml() {
  var userInput = document.getElementById('content').value;
  console.log('User input: ' + userInput);

  var uri = getUri();
  console.log('will be sent to ' + uri);

  putRawXml(getUri(), userInput);
  return false;
}

function putRawXml(uri, xml) {
  var xhr2 = new XMLHttpRequest();
  xhr2.open('PUT', uri, false);
  xhr2.setRequestHeader('Accept', 'application/xml;charset=UTF-8');
  xhr2.setRequestHeader('Content-Type', 'application/xml;charset=UTF-8');

  xhr2.onreadystatechange = function(event) {
    if (xhr2.readyState === 4) {
      if (xhr2.status === 200) {
        var r = xhr2.responseXML;
        console.log("Successfully update the metadata.");
        console.log(r);
        alert('The response was: ' + xhr2.status + ', ' + xhr2.responseText);
      } else if (xhr2.status === 303) {
        alert('redirect');
      } else if (xhr2.status === 0) {
        alert('zero...');
      } else {
        console.log("status: ", xhr2.statusText);
        link
//        alert('Snap, something went wrong: ' + xhr2.status + ', reason: '
//            + xhr2.responseText);
        $(".alert").alert();
      }
    }
  };
  xhr2.send(xml);
}

$(function () {
  $('#pubman-organization-metadata-editor').submit(function (e) {
    e.preventDefault();
    console.log('sending pubman organization metadata...');

    var TEST_URI='http://localhost:8082/rest/v0.9/organizations/escidoc:1/metadata/escidoc?escidocurl=http://esfedrep1.fiz-karlsruhe.de:8080';
    var uri=TEST_URI;

    //TODO fetch the xml from the uri. => var serverXml;
    $.get(uri, function(data){
      console.log('got data: '+data);
    })
    .success(function (data) {
      console.log('success: '+data);
      //in the form, find values that are changed by the user.=> var modifiedKeysArray
      //replace that changed values in the xml. => 
      // for each key in modifiedKeysArray do {
      //    findIn(serverXml, key).value = form.key.value;
      // }
      $('input').each(function() {
        if (this.type !== 'button') {
          if (this.defaultValue !== this.value) {
            $(data).find(this.name).text(this.value);
          }
        }
      });
      // TODO send modifiedServerXml back to the uri
      putRawXml(uri,data);
    })
    .error(function (data) {console.log('failed: '+data);});
    // TODO show notification of the server reaction, success or error
  });
});

$(function() {
  $('#pubman-organization-metadata-editor').submit(
      function(e) {
        e.preventDefault();
        console.log('sending pubman organization metadata...');

        // read user input from the form
        var map = $.serializeForm();

        // build PubMan Metadata XML
        $.get('/rest/pubman-organization-metadata-template.xml').success(
            function(template) {
              console.log('got template: ' + template);

              var payload = template;

              for (key in map) {
                console.log('key: ' + key + ' = ' + map[key]);

                var xmlTagName = $(payload).find(key);
                console.log('xml tag: ' + xmlTagName.text());

                $(payload).find(key).text(map[key]);
              }

              putRawXml(getUri(), payload);
            }).error(function(data) {
          // TODO implement notification
          console.log('error: ' + data);
        })
        return false;
      });
});

jQuery.extend({
  serializeForm : function() {
    var fields = $(":input").serializeArray();
    
    var map = {};
    $.each(fields, function(index, field) {
      map[field.name] = field.value;
    });

    return map;
  }
});