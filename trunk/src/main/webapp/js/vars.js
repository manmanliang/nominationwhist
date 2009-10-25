var game = new Object();
var xmlHttp = new Array();
var user = null;
var userAction = false;
var userId = null;

game.id = null;
game.players = new Array();
game.rounds = new Array();
game.round = new Object();
game.trick = new Object();
game.prevTricksWon = new Array();
game.trick.tricksWon = new Array();
game.trick.cards = new Array();
game.round.current = null;
game.round.currentLastPoll = null;
game.round.count = null;
game.hand = null;
game.phase = null;
game.activePlayer = null;

updated = new Object();
updated.hand = false;
updated.round = false;
updated.trick = false;
updated.players = false;