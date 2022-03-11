var welcomeMsg = 'Hello, World!'

document.querySelector('h1').innerText = welcomeMsg


fetch('/artists').then(resp => resp.json()).then(artists => {
document.querySelector('body').innerHTML = listArtist(artists);
});
let listArtist = function(artist) {
    return '<p>' + artists.artistId + ": " + artists.name + '</p>';
}

function listArtists(json){
    return `
    <div id="artistslist">
        ${json.map(listArtist).join('\n')}
    </div>
    `
}

// function get(url){
//     return new Promise(function(resolve, reject){
//     var req = new XMLHttpRequest();
//     req.open('GET', url);
//     req.onload = function(){
//         if (req.status == 200){
//             resolve(req.response);
//         } else{
//             reject(Error(req.statusText));
//         }
//     }
//     req.onerror = function(){
//         reject(Error("Network error"));
//     }
//     req.send();
//     });
// }

// get('/artists').then(function(response){
//     console.log(JSON.parse(response));
// }, function(err){

// })