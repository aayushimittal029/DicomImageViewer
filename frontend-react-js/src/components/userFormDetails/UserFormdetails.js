import React, { Component } from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import RaisedButton from 'material-ui/RaisedButton';
import TextField from 'material-ui/TextField';
import {Redirect,Link} from "react-router-dom";

export class UserFormdetails extends Component {
    submit = e => {
      //this.props.nextStep();
      console.log("clicked");
      return <Redirect to="/image" />
    };

    render() {
        const { values, handleChange,handleSubmit} = this.props;
        return (
          <MuiThemeProvider>
            <h6 className="title-container__subtitle ">Please fill in the details</h6>
              <TextField
                hintText="Enter your Username"
                floatingLabelText="Username"
                ref="username"
                onChange={handleChange('username')}
                defaultValue={values.username}
              />
              <br />
              <br/>
              <br/>
              <RaisedButton label="Submit"  primary={true} 
                 component={Link} to="/image" onClick={handleSubmit}
              />
            
          </MuiThemeProvider>
        );
      }
    }

    export default UserFormdetails;