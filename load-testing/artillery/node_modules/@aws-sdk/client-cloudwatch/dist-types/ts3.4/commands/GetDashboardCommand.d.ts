import { Command as $Command } from "@smithy/smithy-client";
import { MetadataBearer as __MetadataBearer } from "@smithy/types";
import {
  CloudWatchClientResolvedConfig,
  ServiceInputTypes,
  ServiceOutputTypes,
} from "../CloudWatchClient";
import { GetDashboardInput, GetDashboardOutput } from "../models/models_0";
export { __MetadataBearer };
export { $Command };
export interface GetDashboardCommandInput extends GetDashboardInput {}
export interface GetDashboardCommandOutput
  extends GetDashboardOutput,
    __MetadataBearer {}
declare const GetDashboardCommand_base: {
  new (
    input: GetDashboardCommandInput
  ): import("@smithy/smithy-client").CommandImpl<
    GetDashboardCommandInput,
    GetDashboardCommandOutput,
    CloudWatchClientResolvedConfig,
    ServiceInputTypes,
    ServiceOutputTypes
  >;
  new (
    input: GetDashboardCommandInput
  ): import("@smithy/smithy-client").CommandImpl<
    GetDashboardCommandInput,
    GetDashboardCommandOutput,
    CloudWatchClientResolvedConfig,
    ServiceInputTypes,
    ServiceOutputTypes
  >;
  getEndpointParameterInstructions(): import("@smithy/middleware-endpoint").EndpointParameterInstructions;
};
export declare class GetDashboardCommand extends GetDashboardCommand_base {
  protected static __types: {
    api: {
      input: GetDashboardInput;
      output: GetDashboardOutput;
    };
    sdk: {
      input: GetDashboardCommandInput;
      output: GetDashboardCommandOutput;
    };
  };
}
