
SND = horn

snd:
#	echo "`ls ../snd | sed 's/.wav//'`"
	javac SoundBit.java
	#cvlc ../snd/$(SND).wav vlc://quit 2> /dev/null &
	cd .. ; java proglet.SoundBit # snd/$(SND).wav

doc:
	make -C .. doc
	firefox ../api/proglet/SoundBit.html

all:
	$(MAKE) -C ..
