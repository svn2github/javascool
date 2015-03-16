
function quizzOpen(quizz) {
  /* get page body */
  body = document.body;
  
  /* open transparent background */
  bg = document.createElement('div');
  bg.id = 'questionbg';
  bg.className = 'questionBackground';
  bg.style.display = 'block';
  bg.style.width = body.offsetWidth + 'px';
  bg.style.height = (body.offsetHeight + 20) + 'px';
  body.appendChild(bg);
  
  /* open question window */
  win = document.createElement('div');
  win.id = 'questionwin';
  win.className = 'questionWindow';
  win.style.display = 'block';
  win.style.width = 700;
  //pb avec IE ici win.style.left = 200; // (body.offsetWidth - win.style.width)/2 + 'px';
  body.appendChild(win);
    
  /* display window title bar */
  winTitleBar = document.createElement('div');
  winTitleBar.className = 'questionWinTitleBar';
  win.appendChild(winTitleBar);
  
  /* add close window link */
  winCloseLink = document.createElement('a');
  winCloseLink.href= 'javascript:quizzClose();';
  winTitleBar.appendChild(winCloseLink);

  /* add close window icon */
  winClose = document.createElement('img');
  winClose.src = 'http://javascool.gforge.inria.fr/documents/speed-dating-09/close.gif';
  winClose.className = 'questionWinClose';
  winClose.border = 0;
  winCloseLink.appendChild(winClose);

  /* display the question text */
  winQuestion = document.createElement('div');
  winQuestion.className = 'questionText';
  winQuestion.innerHTML += quizz['question'];
  win.appendChild(winQuestion);
  
  /* display the answers */
  winAnswer = document.createElement('div');
  winAnswer.className = 'questionAnswer';
  win.appendChild(winAnswer);
    
  for (i in quizz['answers']) {
    winAnswers = document.createElement('ul');
    winAnswers.className = 'questionAnswers';

    winAnswersItem = document.createElement('li');   

    winAnswersLink = document.createElement('a');
    winAnswersLink.href = 'javascript:quizzAnswer('+i+','+quizz['answer']+',\''+quizz['link']+'\');';
    winAnswersLink.innerHTML = quizz['answers'][i];

    winAnswersItem.appendChild(winAnswersLink);
    winAnswers.appendChild(winAnswersItem);
    winAnswer.appendChild(winAnswers);
  }
}

function quizzAnswer(answer, response, door) {
  winResult = document.getElementById('questionwin');

  /* build answer space */
  if ( document.getElementById('answer'))
    winResult = document.getElementById('answer');
  else {
    winResult = document.createElement('div');
    winResult.id = 'answer';
  }
  winResult.className = 'questionResult';
  winResult.innerHTML = '';

  if ( answer == response - 1 ) {
    /* good answer */
    winResult.innerHTML = 'Correct';
    win.appendChild(winResult);
  } else {
    /* bad answer */
    winResult.innerHTML = 'Mauvaise r&eacute;ponse';
    win.appendChild(winResult);
  }
  setTimeout('document.location.replace("'+door+'")', 500);

  /* show clock */
  winTimeImage = document.createElement('img');
  winTimeImage.src = 'http://javascool.gforge.inria.fr/documents/speed-dating-09/clock.gif';
  winTimeImage.className = 'questionWinClock';
  winTimeImage.border= 0;
  winResult.appendChild(winTimeImage);
}

function quizzClose() {
  body = document.body;
  body.removeChild(document.getElementById('questionwin'));
  body.removeChild(document.getElementById('questionbg'));
}
