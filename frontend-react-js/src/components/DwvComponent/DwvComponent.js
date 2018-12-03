import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import Typography from "@material-ui/core/Typography";

import Button from "@material-ui/core/Button";
import LinearProgress from "@material-ui/core/LinearProgress";
import Menu from "@material-ui/core/Menu";
import MenuItem from "@material-ui/core/MenuItem";

import ArrowDropDownIcon from "@material-ui/icons/ArrowDropDown";
import IconButton from "@material-ui/core/IconButton";
import CloseIcon from "@material-ui/icons/Close";

import Dialog from "@material-ui/core/Dialog";
import AppBar from "@material-ui/core/AppBar";
import Slide from "@material-ui/core/Slide";
import Toolbar from "@material-ui/core/Toolbar";
import TagsTable from "../TagsTable/TagsTable";
import Navbar from "../navbar/Navbar";
import Annotation from "../annotation/Annotation";
import axios from "axios";
import dwv from "dwv";

// decode query
dwv.utils.decodeQuery = dwv.utils.base.decodeQuery;
// progress
dwv.gui.displayProgress = function () {};
// get element
dwv.gui.getElement = dwv.gui.base.getElement;
// refresh element
dwv.gui.refreshElement = dwv.gui.base.refreshElement;

// Image decoders (for web workers)
dwv.image.decoderScripts = {
    "jpeg2000": "assets/dwv/decoders/pdfjs/decode-jpeg2000.js",
    "jpeg-lossless": "assets/dwv/decoders/rii-mango/decode-jpegloss.js",
    "jpeg-baseline": "assets/dwv/decoders/pdfjs/decode-jpegbaseline.js"
};

function TransitionUp(props) {
  return <Slide direction="up" {...props} />;
}


const styles = theme => ({
  button: {
    margin: theme.spacing.unit,
  },
  appBar: {
    position: 'relative',
  },
  title: {
    flex: '0 0 auto',
  },
  tagsDialog: {
    minHeight: '90vh', maxHeight: '90vh',
    minWidth: '90vw', maxWidth: '90vw',
  },
  iconSmall: {
    fontSize: 20,
  },
});


class DwvComponent extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      versions: {
        dwv: dwv.getVersion(),
        react: React.version
      },
      tools: ['Scroll', 'ZoomAndPan', 'WindowLevel', 'Draw'],
      selectedTool: 'Select Tool',
      loadProgress: 0,
      dataLoaded: true,
      dwvApp: null,
      tags: [],
      showDicomTags: false,
      toolMenuAnchorEl: null,
      dicomImage:null
    };
  }
  handleDrop = e =>{
    console.log(e.target.value)
  }
  handleDocumentUploadChange =event =>{
	
    var fileInput = document.querySelector("#input-file");
    var element = document.getElementById('imageCanvas');
    var element2 = document.getElementById('imageDropBox');
       console.log(element2);
     if (fileInput.files) {
         var file = fileInput.files[0];
         console.log(file.name);
         this.setState({
           dicomImage : file
         });
      //   cornerstone.loadImage(imageId).then(function(image) {
      //    var viewport = cornerstone.getDefaultViewport(element.children[0], image);
      //        cornerstone.displayImage(element, image , viewport);
      //    });
    }
  }
  render() {
    const { classes } = this.props;
    const { versions, tools, loadProgress, dataLoaded, tags, toolMenuAnchorEl } = this.state;
    const toolsMenuItems = tools.map( (tool) =>
      <MenuItem onClick={this.handleMenuItemClick.bind(this, tool)} key={tool} value={tool}>{tool}</MenuItem>
    );

    return (
      <div id="dwv">
        <LinearProgress variant="determinate" value={loadProgress} />
        <div className="button-row">
          <Button variant="raised" color="primary"
            aria-owns={toolMenuAnchorEl ? 'simple-menu' : null}
            aria-haspopup="true"
            onClick={this.handleMenuButtonClick}
            disabled={!dataLoaded}
            className={classes.button}
            size="medium"
          >{ this.state.selectedTool }
          <ArrowDropDownIcon className={classes.iconSmall}/></Button>
          <Menu
            id="simple-menu"
            anchorEl={toolMenuAnchorEl}
            open={Boolean(toolMenuAnchorEl)}
            onClose={this.handleMenuClose}
          >
            {toolsMenuItems}
          </Menu>

          <Button variant="raised" color="primary"
            disabled={!dataLoaded}
            onClick={this.onReset}
          >Reset</Button>

          <Button variant="raised" color="primary"
            onClick={this.handleTagsDialogOpen}
            disabled={!dataLoaded}
            className={classes.button}
            size="medium">Tags</Button>
          <Dialog
            open={this.state.showDicomTags}
            onClose={this.handleTagsDialogClose}
            TransitionComponent={TransitionUp}
            classes={{ paper: classes.tagsDialog }}
            >
              <AppBar className={classes.appBar}>
                <Toolbar>
                  <IconButton color="inherit" onClick={this.handleTagsDialogClose} aria-label="Close">
                    <CloseIcon />
                  </IconButton>
                  <Typography variant="title" color="inherit" className={classes.flex}>
                    DICOM Tags
                  </Typography>
                </Toolbar>
              </AppBar>
              <TagsTable data={tags} />
          </Dialog>
        </div>
        {/* <Annotation /> */}
       
        <div className="layerContainer">
          <div id="imageDropBox" className="dropBox">
          <input id='input-file' multi type='file' onChange={this.handleDocumentUploadChange.bind(null ,this)} />
          <p>Drag and drop data here.</p>
          
          </div>
          <canvas id="imageCanvas" className="imageLayer">Only for HTML5 compatible browsers...</canvas>
          <div className="drawDiv"></div>
        </div>
      </div>
    );
  }

  componentDidMount() {
    // create app
    var app = new dwv.App();
    // initialise app
    app.init({
      "containerDivId": "dwv",
      "tools": this.state.tools,
      "shapes": ["Ruler"],
      "isMobile": true
    });
    // progress
    var self = this;
    app.addEventListener("load-progress", function (event) {
      self.setState({loadProgress: event.loaded});
    });
    app.addEventListener("load-end", function (event) {
      // set data loaded flag
      self.setState({dataLoaded: true});
      // set dicom tags
      self.setState({tags: app.getTags()});
      // set the selected tool
      if (app.isMonoSliceData() && app.getImage().getNumberOfFrames() === 1) {
        self.setState({selectedTool: 'ZoomAndPan'});
      } else {
        self.setState({selectedTool: 'Scroll'});
      }
    });
    // store
    this.setState({dwvApp: app});
  }

  onChangeTool = tool => {
    if ( this.state.dwvApp ) {
      this.setState({selectedTool: tool});
      this.state.dwvApp.onChangeTool({currentTarget: { value: tool } });
    }
  }

  onReset = tool => {
    if ( this.state.dwvApp ) {
      this.state.dwvApp.onDisplayReset();
    }
  }

  handleTagsDialogOpen = () => {
    this.setState({ showDicomTags: true });
  };

  handleTagsDialogClose = () => {
    this.setState({ showDicomTags: false });
  };

  handleMenuButtonClick = event => {
    this.setState({ toolMenuAnchorEl: event.currentTarget });
  };

  handleMenuClose = event => {
    this.setState({ toolMenuAnchorEl: null });
  };

  handleMenuItemClick = tool => {
    this.setState({ toolMenuAnchorEl: null });
    this.onChangeTool(tool);
  };

}

DwvComponent.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(DwvComponent);
