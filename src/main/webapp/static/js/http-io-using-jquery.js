$(function() {
  "use strict";

  $('#pubman-organization-metadata-editor').submit(function(e) {
    e.preventDefault();

    // fetch the metadata as XML
    $.ajax({
      url : '',
      type : 'GET',
      dataType : 'xml',
      success : function(data) {
        console.log('The response was succesful: ' + data);

        // compare user input with the XML.
        $('input').each(function() {
          if (this.type !== 'button') {
            if (this.defaultValue !== this.value) {
              $(data).find(this.name).text(this.value);
            }
          }
        });

        console.log('After: ' + data);
        var foo = data;

        // send the changed XML to the server.
        $.ajax({
          url : '',
          type : 'PUT',
          dataType : 'xml',
          data : '<xml></xml>',
          processData : false,
          success : function(msg) {
            console.log('PUT Request was succesful: ' + msg);
          },
          failure : function(msg) {
            console.log('Failed, reason: ' + msg);
          }
        });
        console.log("test");
      }
    });
  })
});
