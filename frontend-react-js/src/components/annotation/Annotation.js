import React, { Component } from 'react'

export class Annotation extends Component {
  render() {
    return (
      <div style={{border: '1px solid black', width:'400px', float:'right'}}>
      <div className='annotation-details'>
      <h3>Annotations:</h3>
      </div>
      <div className='labels-details'>
        <h3> Coorddinates : </h3>
      </div>
      </div>
    )
  }
}

export default Annotation;
