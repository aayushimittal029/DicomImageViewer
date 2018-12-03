import React, { Component } from 'react';
import {
  BrowserRouter,
  Switch,
  Route,
  Link,
  Redirect
} from 'react-router-dom';
import './App.css';
import Login from './components/Login/Login';
import DwvComponent from './components/DwvComponent/DwvComponent';

class App extends Component {

  render() {

    return (
    <div className='App'>
    <div className ='App-backround-color'>
         <BrowserRouter>
          <Switch>
            <Route path="/" component={Login}/>
            <Route path="/image" component={DwvComponent} /> 
          </Switch>
          </BrowserRouter> 
        </div>
      </div>
    );
  }
}

export default App;
