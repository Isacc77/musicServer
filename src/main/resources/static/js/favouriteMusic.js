$(function () {
    load();
});

//This method can have a parameters or not
function load(musicName) {
    $.ajax({
        url: "/favouritemusic/findfavouritemusic",
        data: {"musicName": musicName},
        type: "GET",
        dataType: "json",
        success: function (obj) {
            console.log(obj);
            var data = obj.data;
            var s = '';
            //data[i].id    data[i].singer  data[i].title
            for (var i = 0; i < data.length; i++) {
                var musicUrl = data[i].url + ".mp3";
                console.log(musicUrl);
                s += '<tr>';
                s += '<td>' + data[i].title + '</td>';
                s += '<td>' + data[i].singer + '</td>';
                s += '<td> <button class = "btn btn-primary"  onclick="playerSong(\'' + musicUrl + '\')"> Play </button>' + '</td>';
                s += '<td> <button class = "btn btn-primary"  onclick="deleteInfo(' + data[i].id + ')"> Remove </button>' + '</td>';
                s += "</tr>"
            }
            $("#info").html(s);
        }
    })
}

function playerSong(obj) {
    var name = obj.substring(obj.lastIndexOf("=") + 1);
    //obj: the address of the music to be played
    //name: the name of the music to be played,
    //0: the start time of the play
    //false: no automatic playback
    SewisePlayer.toPlay(obj, name, 0, true);
}

function deleteInfo(obj) {
    console.log(obj);
    $.ajax({
        url: "/favouritemusic/deletefavouritemusic",
        type: "POST",
        data: {"id": obj},
        dataType: "json",
        success: function (val) {
            console.log(val);
            if (val.data == true) {
                alert("delete successfully! reload current web")
                window.location.href = "list.html";
            } else {
                alert("delete failed");
            }
        }
    });
}

$(function () {
    $("#submit1").click(function () {
        var name = $("#exampleInputName2").val();
        load(name);
    });
});
