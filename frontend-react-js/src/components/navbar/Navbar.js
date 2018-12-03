import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";

const styles = {
  root: {
    flexGrow: 1,
    color: 'white',
    backgroundColor: '#00BCD4'
  },
  grow: {
    flexGrow: 1
  },
};

function Navbar(props) {
  const { classes } = props;
  return (
    <div classes={{ root: classes.root }}>
      <AppBar classes={{ root:classes.root}} position="static">
        <Toolbar>
          <Typography classes={{ root:classes.root}} variant="h6" className={classes.grow}>
            Welcome to ePad Application
          </Typography>
        </Toolbar>
      </AppBar>
    </div>
  );
}

Navbar.propTypes = {
  classes: PropTypes.object.isRequired
};

export default withStyles(styles)(Navbar);






