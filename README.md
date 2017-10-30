# java-content-populator
This is a simple client that allows for bulk file upload to a service accepting multipart requests.

The requirement for this client came from having to populate a project with content in order for the project to have any value. The project required quite a bit of content, and having to manually upload all of those files would have been a hastle and time consuming. As a result, this client was put together. This version was cleaned up and made more generic for anyone to retrofit if they have a similar need.

The entry point of the program takes 5 parameters. 
1. The path to the directory to process. For example: /home/user/files
2. The endpoint to invoke. For example: http://localhost:8080/api/upload
3. The API key to the endpoint. Work needs to be done so that it is not required to provide an API Key.
4. The json template to use as the base payload. For example: 
{
	"id": 1,
	"word": {
		"description": "and",
		"wordClass": "conjunction",
		"wordClassId": "1"
	}
}
5. The http client to use. For example: apache, java8, java9.

# Side note
The project pricked an interest in multipart requests, which is why the project has plans to support various clients that can perform/handle multipart requests. 

## Implemented
* Apache http client

## To be implemented
* Java 8 http client
* Java 9 http2 client
