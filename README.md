# School Project - JAVA skills

## Introduction
This project has been developped for a final at EPSI Paris to demonstrate our JAVA's skills learnt during our 3rd years of bachelor. For this project we played the role of a tech firm hired to conceive and develop a solution for the firm "Go Securi".

## Subject

Go Securi is a security firm which operates in security, guarding, protection and surveillance. Their mission is to assure the security during their clients activities (business trip, manifestation, events).
The firm count 64 employees with 53 security agents

All the agents share a secured room where is stocked materials. Today, there's no automated access control to this local; The agents fill an affectaion papers for each materials they use everyday before their service. This workflow is obsolet and with the rising of new employee it became difficult to manage.

## Project Goals

Our goal was to came up with an automated access solution which will grant access to the local by verifying if the user's face exists in the firm database.

For the realisation of this project, we had to conceive and develop a smart solution which extract and compare a photo taken with a webcam and the photos save in the database.

The taken picture and the access check result has to be displayed to the users. In case of granted access the users could select materials that he'll use during his shift through the solution.

The solution had to be developed in JAVA.


## Project Requirements

* Photo shooting through webcam
* Photo comparison between taken picture and database pictures
* Usage of Google Firebase database with the list of agents and materials
* Development of JAVA application which will take picture, compare it with database and a UI used to grant access and display material acquisition form
* Communication between Google Firebase and the JAVA application

## Solution Type requirement
A Custom-made system of access verification to the local and material association to verified users.


## Our Solution
For this project we (my team and me) made a android application calling AWS face rekognition API to compare photo with databases
