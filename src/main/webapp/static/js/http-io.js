"use strict";

function send() {
  var xhr = new XMLHttpRequest();
  xhr.open('GET', getUri(), false);
  xhr.setRequestHeader('Accept', 'application/xml;charset=UTF-8');
  xhr.setRequestHeader('Content-Type', 'application/xml;charset=UTF-8');

  xhr.onreadystatechange = function(event) {
    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        var r = xhr.responseText;
        var rXml = xhr.responseXML;
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

  var uri = getUri();

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
        alert('The response was: ' + xhr2.status + ', ' + xhr2.responseText);
      } else if (xhr2.status === 303) {
        alert('redirect');
      } else if (xhr2.status === 0) {
        alert('zero...');
      } else {
        alert("status: ", xhr2.statusText);
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

    var TEST_URI='http://localhost:8082/rest/v0.9/organizations/escidoc:1/metadata/escidoc?escidocurl=http://esfedrep1.fiz-karlsruhe.de:8080';
    var uri=TEST_URI;

    //TODO fetch the xml from the uri. => var serverXml;
    $.get(uri, function(data){
      //
    })
    .success(function (data) {
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
    .error(function (data) {alert('failed: '+data);});
    // TODO show notification of the server reaction, success or error
  });
});

$(function() {
  $('#pubman-organization-metadata-editor').submit(
    function(e) {
    e.preventDefault();

    // read user input from the form
    var map = $.serializeForm();

    // build PubMan Metadata XML
    $.get('/rest/pubman-organization-metadata-template.xml').success(
      function(template) {

      var payload = template;

      for (var key in map) {

        var xmlTagName = $(payload).find(key);

        $(payload).find(key).text(map[key]);
      }

      putRawXml(getUri(), payload);
    }).error(function(data) {
      // TODO implement notification
      alert('error: ' + data);
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

function serializePubmanContextForm(){
  var map={};
  map['genres']= $('input[name="genresList"]:checked');
  map['subjects']=$('input[name="subjectList"]:checked');
  map['workflow']=$('#workflow option:selected');
  map['schema']=$('#validation-schema option:selected');
  map['email']=$('input[name="contact-email"]');
  return map;
}

$(function() {
  $('#pubman-context-metadata-editor').submit(
    function(e) {
    e.preventDefault();

    var map = serializePubmanContextForm();
    $.get('/rest/pubman-context-metadata-template.xml').success(
      function(template) {
      var payload = template;

      // when the user does not select any genres, remove the element the node '<allowed-genres/>'
      // other wise, add the selected genres as: <allowed-genre>$(value)</allowed-genre>
      var selectedGenres=map['genres']; 
      var genreSize= selectedGenres.length;
      if(genreSize) {
        $.each(selectedGenres, function(index, genre){
          $(payload).find('allowed-genres').append($('<allowed-genre>').text(genre.value)); 
        });
      } else{
        $(payload).find('allowed-genres').remove();
      }


      //FIXME refactor to function
      var selectedSubjects=map['subjects']; 
      if(selectedSubjects.length) {
        $.each(selectedSubjects, function(index, subject){
          $(payload).find('allowed-subject-classifications').append($('<allowed-subject-classification>').text(subject.value)); 
        });
      } else{
        $(payload).find('allowed-subject-classification').remove();
      }

      $(payload).find('validation-schema').text(map['schema'].val());
      $(payload).find('workflow').text(map['workflow'].val());
      
      if(map['email'].val()){
	      $(payload).find('contact-email').text(map['email'].val());
      } else{
	      $(payload).find('contact-email').remove();
      }

      putRawXml(getUri(), payload);
    }).error(function(data) {
      // TODO implement notification
      alert('error: ' + data);
    })
    return false;
  });
});


//TODO When the user clicks the save button, she should be able to update the pubman context metadata in the escidoc core.
//TODO 1. read the user input that changes
//TODO 2. collect them
//what to collect:
// 1. Genres 			multiple	
// 2. Subjects			multiple
// 3. Workflow			single
// 4. Validation Schema single
// 5. Contact Email 	single
//TODO 3. serialize the values to XML
//TODO    a. write the xml using JavaScript
//TODO    b. load a xml template, remove unneccassry element