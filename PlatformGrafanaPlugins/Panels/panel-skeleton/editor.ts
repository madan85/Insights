///<reference path="../../../headers/common.d.ts" />


import _ from 'lodash';
import $ from 'jquery';
import moment from 'moment';
import angular from 'angular';
import kbn from 'app/core/utils/kbn';
import { PanelPathGenerator } from '../insightsCore/PanelPathGenerator';

export class PipelinePanelEditorCtrl {
  pipelinePanel: any;
  pipelinePanelCtrl: any;
  pipelinePanelMetaData: any;
  dataSourceResponse: any;
  /** @ngInject */
  constructor($scope, private $q, private uiSegmentSrv) {
    //$scope.editor = this;
    this.pipelinePanelCtrl = $scope.ctrl;
    this.pipelinePanel = this.pipelinePanelCtrl.panel;
    this.pipelinePanelMetaData = this.pipelinePanel.pipelinePanelMetaData;
    this.dataSourceResponse = this.pipelinePanelCtrl.dataSourceResponse;
  }

  //Use render method for refreshing the view.
  render() {
    this.pipelinePanelCtrl.render();
  }
}

/** @ngInject */
export function pipelinePanelEditor($q, uiSegmentSrv) {
  'use strict';
  var pathClassObject = new PanelPathGenerator();
  var GrafanaPluginPath = pathClassObject.getPathDetails();
  return {
    restrict: 'E',
    scope: true,
    templateUrl: GrafanaPluginPath + '/pipeline/editor.html',
    controller: PipelinePanelEditorCtrl,
    controllerAs: 'pipelinePanelEditorCtrl'
  };
}
