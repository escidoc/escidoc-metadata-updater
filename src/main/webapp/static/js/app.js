$(function() {
  "use strict";

  $(".alert").hide();

  $("#select-all-genres").click(function () {
    $("#genre input").attr("checked", true);
  });

  $("#unselect-all-genres").click(function () {
    $("#genre input").attr("checked", false);
  });

  $("#select-all-subjects").click(function () {
    $("#subject input").attr("checked", true);
  });

  $("#unselect-all-subjects").click(function () {
    $("#subject input").attr("checked", false);
  });
});
