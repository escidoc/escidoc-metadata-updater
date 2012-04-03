"use strict";
//TODO general todo: introduce nama spacing and object-oriented JavaScript

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

function putRawXml(uri, xml) {
    $.ajax({
        type: "PUT",
        dataType: "application/xml",
        contentType: "application/xml",
        url: uri,
        data: xml 
    })
    .success(function(msg){
        alert('The response was: ' + xhr2.status + ', ' + xhr2.responseText);
    })
    .error(function(msg){
        alert('The response was: ' + xhr2.status + ', ' + xhr2.responseText);
    });
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

    // create an XML Document
    var $root = $('<XMLDocument/>');
    $root
      .append($('<pubman-admin-descriptor />'));

    // when the user does not select any genres, remove the element the node '<allowed-genres/>'
    // other wise, add the selected genres as: <allowed-genre>$(value)</allowed-genre>
    //FIXME refactor to function
    var selectedGenres=map['genres'];
    var genreSize= selectedGenres.length;
    if(genreSize) {
      $root.find('pubman-admin-descriptor')
        .append($('<allowed-genres />'));
      $.each(selectedGenres, function(index, genre){
      $root.find('allowed-genres')
        .append($('<allowed-genre />').text(genre.value));
      });
    } 

    //FIXME refactor to function
    var selectedSubjects=map['subjects'];
    if(selectedSubjects.length) {
      $root.find('pubman-admin-descriptor')
        .append($('<allowed-subject-classifications />'));
      $.each(selectedSubjects, function(index, subject){
        $root.find('allowed-subject-classifications')
          .append($('<allowed-subject-classsification />').text(subject.value));
      });
    } 

    //FIXME refactor to function
    $root.find('pubman-admin-descriptor')
      .append($('<validation-schema />').text(map['schema'].val()));

    //FIXME refactor to function
    $root.find('pubman-admin-descriptor')
      .append($('<workflow />').text(map['workflow'].val()));

    //FIXME refactor to function
    if(map['email'].val()){
      $root.find('pubman-admin-descriptor')
        .append($('<contact-email />').text(map['email'].val()));
    } 

    putRawXml(getUri(), $root.html());
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
// TODO rewrite with jQuery Ajax
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
  putRawXml(getUri(),xml);
}
