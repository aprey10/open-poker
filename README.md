[![GitHub license](https://img.shields.io/github/license/aprey10/open-poker)](https://github.com/aprey10/open-poker/blob/main/LICENSE)

<p align="center">
    <img src="https://user-images.githubusercontent.com/4034723/104251770-71453a80-543e-11eb-8a7b-9bb0bebbb0f9.png"><br/>
    Jira plugin which adds simple and handy Planning (Scrum) Poker to the Issue View page.
</p>

[![Demo](https://img.youtube.com/vi/61iEyZg8JZw/maxresdefault.jpg)](https://youtu.be/x3JumyxaRho)

### Actions description

**Start Estimation** - starts a new estimation session for the current jira issue. The jira issue can have only one estimation session in progress.

**Vote** - submit an estimate. The user can estimate only once during the session.

**Stop Estimation** - stops active estimation. Only the user that started estimation can stop it.

**Re-estimate** - starts a new estimation session.

**Apply** - applies final estimate and populates 'Story Points' field (if it's present and final estimate is numeric)

**Terminate estimation** - terminates estimation session.

### Supported estimation scales

**Planning Cards** - Classic planning poker sequence: 0, 1, 2, 3, 5, 8, 13, 20, 40, 100, ?, Coffee, Infinite.

**Fibonacci** - Fibonacci sequence: 1, 2, 3, 5, 8, 13, 21, ?, Coffee, Infinite.

**Linear** - Increments in a fixed value (1): 1, 2, 3,... 12

**T-shirt size** - XS, S, M, L, XL, XXL, XXXL


### Data security and privacy statement

This plugin doesn't store or use any customer data.
