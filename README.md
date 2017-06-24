# Strange Face Maker
This project is for Global AI Hackathon.
When you feel angry/disgust/sadness, this system takes a photo and creates funny pictures. 

This system uses 
- Microsoft Face API
- Microsoft Image Search API
- Apitore Word2Vec API

Algorithm is 
1. Take a photo
2. Call "Face API" and detect a face location and face features and face emotions. If the score of a certain feeling (angry/disgust/sadness) is exceeded, go next.
3. Make a vector according to face features, and then call "Word2Vec API" to select a funny word. This process is a kind of random factor.
4. Call "Image Search API" with word above, and download a funny picture.
5. Alpha blending with two pictures: your face and funny picture.

## Requirement
- JDK 1.8.0-102 upper
- Maven 3 upper