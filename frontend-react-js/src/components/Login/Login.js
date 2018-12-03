import React, { Component } from 'react';
import upload from "../../Images/upload.png";
import UserFormdetails from '../userFormDetails/UserFormdetails';
import {Redirect} from "react-router-dom";
import axios from "axios";
import DwvComponent from '../DwvComponent/DwvComponent';
class Login extends Component {
constructor(props){
 super(props); 
 this.state={
    username: '',
    userdata:'',
    showDiv :false
  };
}

  handleChange = input => e => {
    this.setState({[input]: e.target.value})
  };
  handleSubmit = e => {
    e.preventDefault();
    
      this.setState({
      showDiv:true
      });
  }

 render() {
    const { username} = this.state;
    const values = {username};
        return (
          <div>
        
           {!this.state.showDiv  && (
             <div className ='card-layout'>
           <div><h1 className="title-container__title">Welcome to ePad application</h1>
		         <h3 className="title-container__subtitle ">Find out more about dicom images</h3>
		            <div className="photos" style={{ textAlign: 'center' }}>
                <img src={upload} alt ="" width="60" height="50" />
                </div>
                
                <UserFormdetails
                handleChange={this.handleChange}
                values={values}
                handleSubmit={this.handleSubmit}
                />
               
                </div>
                </div>
                )
                }
                {this.state.showDiv  && (<DwvComponent username={this.state.username}/>) }
                
          </div>
        
        );
    }
}

export default Login;