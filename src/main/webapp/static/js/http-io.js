"use strict";

// PubMan Organization Metadata/Admin Descriptor
$(function() {
  $('#pubman-organization-metadata-editor')
      .submit(function(e) {
        e.preventDefault();
    
        // read user input from the form
        var map = $.serializeForm();
    
        // build PubMan Metadata XML
        $.get('/rest/pubman-organization-metadata-template.xml')
            .success(
              function(template) {
            	  
              var payload = template;
              
              for (var key in map) {
                var xmlTagName = $(payload).find(key);
                $(payload).find(key).text(map[key]);
              }
        
              putRawXml(getUri(), payload);
            })
            .error(function(data) {
              // TODO implement notification
              alert('error: ' + data);
            })
        return false;
  });
});

// TODO rewrite with jQuery
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
        $(".alert").alert();
      }
    }
  };
  xhr2.send(xml);
}

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

// PubMan Context Metadata/Admin Descriptor
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
          $(payload)
          .find('allowed-genres')
          .append($('<allowed-genre />')
          .text(genre.value)); 
        });
      } else{
        $(payload).find('allowed-genres').remove();
      }


      //FIXME refactor to function
      var selectedSubjects=map['subjects']; 
      if(selectedSubjects.length) {
        $.each(selectedSubjects, function(index, subject){
          $(payload)
            .find('allowed-subject-classifications')
            .append($('<allowed-subject-classification>')
            .text(subject.value)); 
        });
      } else{
        $(payload).find('allowed-subject-classifications').remove();
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

function serializePubmanContextForm(){
  var map={};
  map['genres']= $('input[name="genresList"]:checked');
  map['subjects']=$('input[name="subjectList"]:checked');
  map['workflow']=$('#workflow option:selected');
  map['schema']=$('#validation-schema option:selected');
  map['email']=$('input[name="contact-email"]');
  return map;
}

// Raw Metadata
// TODO rewrite with jQuery
function sendRawXml() {
  var userInput = document.getElementById('content').value;
  var uri = getUri();
  putRawXml(getUri(), userInput);
  return false;
}

// TODO rewrite with jQuery
function getUri() {
  return window.location.href;
}


// Generic Metadata Editor as flat key:value pairs.
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