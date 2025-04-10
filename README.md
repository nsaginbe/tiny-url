<h1>TinyUrl</h1>

TinyUrl is a web service that converts long, complex URLs into short, easily shareable links. Supports custom links btw!

<h3>API Endpoints</h3>

- POST api/shorten

  Params are url (required) and custom_short_url (not required). Shortens any length url to fixed 6-size easy memorizable links.
  
- GET api/r/{short_url}

  Redirects the user to an original url.
