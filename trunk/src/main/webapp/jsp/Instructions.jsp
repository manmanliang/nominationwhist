 	<div id="instructions" style="display: none;">
 		<p id="instructionsInternalLink">
 			<a href="javascript:showInstructions('gameInstructions')" id="gameInstructionsLink">Rules</a>
 			<a href="javascript:showInstructions('boardInstructions')" id="boardInstructionsLink">Game Screen Help</a>
 			| <a href="javascript:toggleInstructions()">Hide Instructions</a>
 		</p>
 		<div id="gameInstructions">
	 		<h2>Game Rules</h2>
	 		<p>Nomination whist is played as a series of rounds in which:</p>
	 		<ol>
	 			<li>each player bids</li>
	 			<li>a player chooses trumps</li>
	 			<li>tricks are played until all cards have been played</li>
	 			<li>scores are added up</li>
	 		</ol>
	 		<h3>Bidding</h3>
	 		<p>Rounds starts with each player bidding, starting with the player to the left of the previous first bidder</p>
	 		<p>In each round the last player to bid may not make a bid which would result in the total bids being equal to the number of cards dealt to each player</p>
	 		<p>No player may bid zero more than 3 times in a row</p>
	 		<h3>Choosing Trumps</h3>
	 		<p>After all players have made their bid, the player who bid the highest then chooses trumps</p>
	 		<h3>Tricks</h3>
	 		<p>Once trumps have been chosen each player takes turns playing a card starting with the player who bid first, followed in consecutive tricks by the person who won the previous trick</p>
	 		<p>After the first player has played their card, subsequent players must:</p>
	 		<ol>
	 			<li>Play a card of the same suit as the first card in the trick if they can</li>
	 			<li>Otherwise play a card of a different suit, trumps or not</li>
	 		</ol>
	 		<p>The winner of the trick is the person who played the highest card in the suit that was played first unless any trumps have been played in which case the highest trump card wins</p>
	 		<h3>Scoring</h3>
	 		<p>For each round each player scores 1 point for every trick they won, plus 10 extra points if they won the same number of tricks as they won</p>
	 	</div>
		<div id="boardInstructions" style="display: none;">
	 		<h2>Game Board</h2>
	 		<p>This page explains the sections of the game area and what they mean:</p>
	 		<h3>Header</h3>
	 		<p>On the left is the name of the player and a link to these instructions</p>
 			<p>On the right is a scoreboard showing the following information the previous round followed by the current round consisting of:</p>
 			<ul>
 				<li>The number of cards in the round</li>
 				<li>What trumps were chosen</li>
 				<li>And for each player:</li>
 				<ul>
 					<li>Their bid</li>
 					<li>The number of tricks they won</li>
 					<li>Their total score</li>
 				</ul>
 			</ul>
	 		<h3>Main Area</h3>
 			<p>On the left is the game table holding the cards of this trick, and bottom right the cards that were played in the previous trick</p>
 			<p>On the right is a space for:</p>
 			<ol>
 				<li>Status messages from the server about the game</li>
 				<li>Chat messages from other players - To write a message, type in the box at the bottom and press return</li>
 			</ol>
 			<h3>Hand</h3>
 			<p>At the bottom of the screen are your current cards</p>
		</div>
	</div>
